import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

@Platform(include="<algorithm>")
@Namespace("std")
public class Mullion {
    static { Loader.load(); }

    public static class Callback extends FunctionPointer {
        // Loader.load() and allocate() are required only when explicitly creating an instance
        static { Loader.load(); }
        protected Callback() { allocate(); }
        private native void allocate();

        public @Name("mullion") boolean call(int a, int b) throws Exception {
            throw new Exception("bar " + a * b );
        }
    }
}
