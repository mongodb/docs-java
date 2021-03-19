package fundamentals.monolightcodec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;


// start class
public class PowerStatusCodec implements Codec<String> {

    @Override
    public void encode(BsonWriter writer, String value, EncoderContext encoderContext) {
        if (value.equals("on")) {
            writer.writeBoolean(Boolean.TRUE);;
        } else if (value.equals("off")) {
            writer.writeBoolean(Boolean.FALSE);
        } else {
            writer.writeNull();
        }
    }

    @Override
    public Class<String> getEncoderClass() {
        return String.class;
    }

    @Override
    public String decode(BsonReader reader, DecoderContext decoderContext) {
        Boolean value = reader.readBoolean();

        if (value) {
            return "on";
        } else if (!value) {
            return "off";
        }
        return null;
    }
}
// end class
