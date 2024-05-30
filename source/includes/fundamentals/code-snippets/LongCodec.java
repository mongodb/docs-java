import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

// start class
public class LongCodec implements Codec<Long> {

    @Override
    public Long decode(BsonReader reader, DecoderContext decoderContext) {
        return reader.readDateTime();
    }

	@Override
	public void encode(BsonWriter writer, Long value, EncoderContext encoderContext) {
        if (value != null) {
            writer.writeDateTime(value);
        }
	}

	@Override
	public Class<Long> getEncoderClass() {
		return Long.class;
	}
}
// end class
