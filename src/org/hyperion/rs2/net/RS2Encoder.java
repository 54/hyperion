package org.hyperion.rs2.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hyperion.rs2.model.Player;


/**
 * Game protocol encoding class.
 * @author Graham
 *
 */
public class RS2Encoder implements ProtocolEncoder {

	@Override
	public void encode(IoSession session, Object in, ProtocolEncoderOutput out) throws Exception {
		synchronized(session) {
			Packet p = (Packet) in;
			if(p.isRaw()) {
				out.write(p.getPayload());
			} else {
				ISAACCipher outCipher = ((Player) session.getAttribute("player")).getOutCipher();
				
				int opcode = p.getOpcode();
				Packet.Type type = p.getType();
				int length = p.getLength();
				
				opcode += outCipher.getNextValue();
				
				int finalLength = length + 1;
				switch(type) {
				case VARIABLE:
					finalLength += 1;
					break;
				case VARIABLE_SHORT:
					finalLength += 2;
					break;
				}
				
				IoBuffer buffer = IoBuffer.allocate(finalLength);
				buffer.put((byte) opcode);
				switch(type) {
				case VARIABLE:
					buffer.put((byte) length);
					break;
				case VARIABLE_SHORT:
					buffer.putShort((short) length);
					break;
				}
				buffer.put(p.getPayload());
				out.write(buffer.flip());
			}
		}
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		
	}
	
}
