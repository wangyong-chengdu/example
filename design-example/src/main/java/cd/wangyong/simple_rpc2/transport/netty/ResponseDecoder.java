package cd.wangyong.simple_rpc2.transport.netty;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import cd.wangyong.simple_rpc2.transport.command.Header;
import cd.wangyong.simple_rpc2.transport.command.ResponseHeader;

/**
 * @author andy
 * @since 2020/10/13
 */
public class ResponseDecoder extends CommandDecoder {
    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        int type = byteBuf.readInt();
        int version = byteBuf.readInt();
        int requestId = byteBuf.readInt();

        int code = byteBuf.readInt();
        int errorLength = byteBuf.readInt();
        byte[] errorBytes = new byte[errorLength];
        byteBuf.readBytes(errorBytes);
        String error = new String(errorBytes, StandardCharsets.UTF_8);

        return new ResponseHeader(type, version, requestId, code, error);
    }
}
