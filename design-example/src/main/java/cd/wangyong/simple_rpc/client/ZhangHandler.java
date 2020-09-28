package cd.wangyong.simple_rpc.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
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
 *
 * @author andy
 * @since 2020/9/27
 */
public class ZhangHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(ZhangHandler.class);
    private static final PackageCodeC packageCodeC = new PackageCodeC(SerializeType.PROTOBUF.getCode(), MessageType.TALK.getCode());

    private static AtomicInteger counter = new AtomicInteger(0);
    private static final Map<String, Channel> CHANNEL_MAPPING = new ConcurrentHashMap<>();

    private static final Map<Byte, TalkMessage> talkContext = new HashMap<>();

    static {
        talkContext.put((byte) 1, new TalkMessage(MessageType.TALK, PackageType.REQUEST, (byte) 1, "张大爷", "吃了没，您呐？"));
        talkContext.put((byte) 2, new TalkMessage(MessageType.TALK, PackageType.RESPONSE, (byte) 2, "张大爷", "嗨，没事儿溜溜弯儿。"));
        talkContext.put((byte) 3, new TalkMessage(MessageType.TALK, PackageType.RESPONSE, (byte) 3, "张大爷", "回头去给老太太请安！"));
    }

    public void invoke(String host) throws CloneNotSupportedException {
        TalkMessage message = (TalkMessage) talkContext.get((byte) 1).clone();
        ByteBuf byteBuf = packageCodeC.encode(message.setRoundId(counter.getAndIncrement()));
        CHANNEL_MAPPING.get(host).writeAndFlush(byteBuf);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CHANNEL_MAPPING.put(getHost(ctx), ctx.channel());
        super.channelActive(ctx);
    }

    private static String getRemoteAddress(ChannelHandlerContext ctx) {
        return Optional.ofNullable(ctx.channel().remoteAddress().toString()).orElse("").replace("/", "");
    }

    private static String getHost(ChannelHandlerContext ctx) {
        String address = getRemoteAddress(ctx);
        return address.substring(0, address.indexOf(":"));
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
        if (sessionId == (byte) 2) {
            TalkMessage sessionTwoResponse = (TalkMessage) talkContext.get(2).clone();
            ctx.writeAndFlush(packageCodeC.encode(sessionTwoResponse.setRoundId(message.getRoundId())));
        } else if (sessionId == (byte) 3) {
            TalkMessage sessionThreeResponse = (TalkMessage) talkContext.get(3).clone();
            ctx.writeAndFlush(packageCodeC.encode(sessionThreeResponse.setRoundId(message.getRoundId())));
        }
    }
}
