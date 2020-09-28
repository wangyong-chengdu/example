package cd.wangyong.simple_rpc.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import cd.wangyong.simple_rpc.protocol.Message;
import cd.wangyong.simple_rpc.protocol.MessageType;
import cd.wangyong.simple_rpc.protocol.PackageCodeC;
import cd.wangyong.simple_rpc.protocol.PackageType;
import cd.wangyong.simple_rpc.protocol.TalkMessage;
import cd.wangyong.simple_rpc.serialization.SerializeType;

/**
 * 业务处理请求
 * @author andy
 * @since 2020/9/27
 */
public class LiHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(LiHandler.class);
    private static final PackageCodeC packageCodeC = new PackageCodeC(SerializeType.PROTOBUF.getCode(), MessageType.TALK.getCode());
    private static final Map<Byte, TalkMessage> talkContext = new HashMap<>();
    static {
        talkContext.put((byte) 1, new TalkMessage(MessageType.TALK, PackageType.RESPONSE, (byte) 1, "李大爷", "刚吃"));
        talkContext.put((byte) 2, new TalkMessage(MessageType.TALK, PackageType.REQUEST, (byte) 2, "李大爷", "您这，嘛去？"));
        talkContext.put((byte) 3, new TalkMessage(MessageType.TALK, PackageType.REQUEST, (byte) 3, "李大爷", "有空家里坐坐啊。"));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Message message = packageCodeC.decode(byteBuf);
        _do(ctx, (TalkMessage) message);
    }

    private void _do(ChannelHandlerContext ctx, TalkMessage message) throws CloneNotSupportedException {
//        logger.info(message);
        System.out.println(message);

        byte sessionId = message.getSessionId();
        if (sessionId == (byte) 1) {
            TalkMessage sessionOneResponse = (TalkMessage) talkContext.get((byte) 1).clone();
            ctx.writeAndFlush(packageCodeC.encode(sessionOneResponse.setRoundId(message.getRoundId())));

            TalkMessage sessionTwoRequest = (TalkMessage) talkContext.get((byte) 2).clone();
            ctx.writeAndFlush(packageCodeC.encode(sessionTwoRequest.setRoundId(message.getRoundId())));
        }
        else if (sessionId == (byte) 2) {
            TalkMessage sessionThreeRequest = (TalkMessage) talkContext.get((byte) 3).clone();
            ctx.writeAndFlush(packageCodeC.encode(sessionThreeRequest.setRoundId(message.getRoundId())));
        }
    }
}
