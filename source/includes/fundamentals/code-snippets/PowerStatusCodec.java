package fundamentals.monolightcodec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;


// start class
public class PowerStatusCodec implements Codec<PowerStatus> {

    @Override
    public void encode(BsonWriter writer, PowerStatus value, EncoderContext encoderContext) {
        if (value != null) {
            writer.writeBoolean(value.equals(PowerStatus.ON) ? Boolean.TRUE : Boolean.FALSE);
        }
    }

    @Override
    public PowerStatus decode(BsonReader reader, DecoderContext decoderContext) {
        Boolean value = reader.readBoolean();

        if (value) {
            return PowerStatus.ON;
        } else if (!value) {
            return PowerStatus.OFF;
        }
        return null;
    }

    @Override
    public Class<PowerStatus> getEncoderClass() {
        return PowerStatus.class;
    }
}
// end class
