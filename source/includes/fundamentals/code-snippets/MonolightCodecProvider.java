package fundamentals.monolightcodec;

import org.bson.codecs.Codec;
import org.bson.codecs.IntegerCodec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;


// start MonolightCodec class
public class MonolightCodecProvider implements CodecProvider {
    private MonolightCodec monolightCodec;
    private IntegerCodec integerCodec;
    private PowerStatusCodec powerStatusCodec;
    
    public MonolightCodecProvider() {
        integerCodec = new IntegerCodec();
        powerStatusCodec = new PowerStatusCodec();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == Monolight.class) {
            return (Codec<T>) new MonolightCodec(registry);
        } else if (clazz == Integer.class) {
            return (Codec<T>) integerCodec;
        } else if (clazz == String.class) {
            return (Codec<T>) powerStatusCodec;
        }
        
        // return null when not a provider for the requested class 
        return null;
    }

}
// end MonolightCodec class
