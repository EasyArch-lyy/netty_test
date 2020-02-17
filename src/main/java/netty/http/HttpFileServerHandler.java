package netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpMethod.CONNECT;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String url;

    public HttpFileServerHandler(String url){
        this.url=url;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //对HTTP请求消息解码结果判断，解码失效构造404返回
        if (!request.getDecoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }
        //对请求行中的方法判断
        if (request.getMethod() != GET) {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }
        final String uri = request.getUri();
        System.out.println("uri"+uri);
        final String path = sanitizeUri(uri);
        System.out.println("path"+path);
        if (path == null) {
            sendError(ctx, FORBIDDEN);
            return;
        }
        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            sendError(ctx,NOT_FOUND);
            return;
        }
        if(file.isDirectory()){
            //
            if(uri.endsWith("/")){
                sendListing(ctx,file);
            }else {
                sendRedirect(ctx,uri+'/');
            }
            return;
        }
        if (!file.isFile()) {
            sendError(ctx, FORBIDDEN);
            return;
        }
        RandomAccessFile randomAccessFile=null;
        try {
            //以只读形式打开文件
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException fnfe) {
            sendError(ctx, NOT_FOUND);
            return;
        }
        //获取文件长度，构造成功的HTTP应答消息，消息头设置content-length和content type
        long fileLength = randomAccessFile.length();
        HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        setContentLength(response, fileLength);
        setContentTypeHandler(response, file);
        if (isKeepAlive(request)) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.write(response);
        ChannelFuture sendFileFuture;
        sendFileFuture=ctx.write(new ChunkedFile(randomAccessFile,0,fileLength,8192),ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {

            @Override
            public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long progress, long total) throws Exception {

                if (total < 0) {
                    System.err.println("Transfer progress: " + progress);
                } else {
                    System.err.println("Transfer progress: " + progress + "/" + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                System.out.println("Transfer complete.");
            }
        });
        ChannelFuture lastContentFuture =ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if(!isKeepAlive(request)){
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        if(ctx.channel().isActive()){
            sendError(ctx,INTERNAL_SERVER_ERROR);
        }
    }

    private static final Pattern INSECURE_URI=Pattern.compile(".*[<>&\"].*");

    private String sanitizeUri(String uri){
        try {
            //对URL解码
            uri=URLDecoder.decode(uri,"UTF-8");
        }catch (UnsupportedEncodingException e){
            try {
                uri= URLDecoder.decode(uri,"ISO-8859-1");
            }catch (UnsupportedEncodingException e1){
                throw new Error();
            }
        }
        if(!uri.startsWith(uri)){
            return null;
        }
        if(!uri.startsWith("/")){
            return null;
        }
        //将硬编码的文件路径分隔符替换为本地文件操作系统文件路径分隔符
        //File.separator当前文件分隔符
        uri = uri.replace('/', File.separatorChar);
        //对新的uri做合法校验
        if(uri.contains(File.separator+'.')
                ||uri.contains(File.separator+'.')
                ||uri.startsWith(".")
                ||uri.endsWith(".")
                ||INSECURE_URI.matcher(uri).matches()){
            return null;
        }
        System.out.println("当前运行程序工程目录"+System.getProperty("user.dir")+File.separator);
        System.out.println("System.getProperty:"+System.getProperty("user.dir"));
        //文件拼接，使用当前运行程序工程目录
//        return System.getProperty("user.dir")+File.separator+uri;
        //返回文件路径
        return "/home/lyy"+uri;
    }

    private static final Pattern ALLOWED_FILE_NAME=Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
    
    private static void sendListing(ChannelHandlerContext ctx,File dir){
        //创建成功的HTTP响应消息
        FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1,OK);
        response.headers().set(CONTENT_TYPE,"text/html;charset=UTF-8");
        //构造响应消息体
        StringBuilder buf =new StringBuilder();
        String dirPath=dir.getPath();
        buf.append("<html><head><title>");
        buf.append(dirPath);
        buf.append("目录： ");
        buf.append("</title></head><body>\r\n");
        buf.append("<h3>");
        buf.append(dirPath).append("目录： ");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        //打印链接
        buf.append("<li>链接<a href=\"../\">..</a></li>\r\n");
        //展示根目录下所有文件和文件夹
        for(File f:dir.listFiles()){
            if(f.isHidden()||!f.canRead()){
                continue;
            }
            String name=f.getName();
            if(!ALLOWED_FILE_NAME.matcher(name).matches()){
                continue;
            }
            buf.append("<li>链接: <a href=\"");
            buf.append(name);
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");
        }
        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer= Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        //缓冲区消息放到HTTP应答消息中，释放缓冲区
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendRedirect(ChannelHandlerContext ctx,String newUri){
        FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1,FOUND);
        response.headers().set(LOCATION,newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1,status,Unpooled.copiedBuffer("Failure "+status.toString()+"\r\n",CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE,"text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void setContentTypeHandler(HttpResponse response, File file) {
        MimetypesFileTypeMap mimetypesFileTypeMap=new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE,mimetypesFileTypeMap.getContentType(file.getPath()));
    }
}
