package fundamentals;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class AnswersCodec implements Codec<String> {

    @Override
    public void encode(BsonWriter writer, String value, EncoderContext encoderContext) {
        if (value.equals("correct")) {
            writer.writeBoolean(Boolean.TRUE);
        } else if (value.equals("incorrect")) {
            writer.writeBoolean(Boolean.FALSE);
        } else {
            writer.writeNull();
        }
    }

    @Override
    public String decode(BsonReader reader, DecoderContext decoderContext) {
        Boolean value = reader.readBoolean();

        if (value) {
            return "correct";
        } else if (!value) {
            return "incorrect";
        } else {
            return null;
        }
    }

    @Override
    public Class<String> getEncoderClass() {
        return String.class;
    }

}
