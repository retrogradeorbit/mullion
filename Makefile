GRAALVM_HOME = $(HOME)/graalvm-ce-java11-20.1.0
JAVA_HOME = $(GRAALVM_HOME)
PATH = $(GRAALVM_HOME)/bin:$(shell echo $$PATH)
SRC = src/mullion/core.clj
VERSION = 0.1.0-SNAPSHOT

all: build/mullion

clean:
	-rm -rf build target src/cpp/linux-x86_64 src/cpp/jniMullion.h \
		src/cpp/jni*.cpp src/cpp/*.class mullion
	lein clean

target/uberjar/mullion-$(VERSION)-standalone.jar: $(SRC)
	-mkdir graal-configs
	GRAALVM_HOME=$(GRAALVM_HOME) lein uberjar

analyse:
	$(GRAALVM_HOME)/bin/java -agentlib:native-image-agent=config-output-dir=config-dir \
		-Dorg.bytedeco.javacpp.logger.debug=false \
		-Djava.library.path=$$HOME/.mullion/libs/ \
		-jar target/uberjar/mullion-$(VERSION)-standalone.jar

build/mullion: target/uberjar/mullion-$(VERSION)-standalone.jar
	-mkdir build
	export
	$(GRAALVM_HOME)/bin/native-image \
		-jar target/uberjar/mullion-$(VERSION)-standalone.jar \
		-H:Name=build/mullion \
		-H:+ReportExceptionStackTraces \
		-J-Dclojure.spec.skip-macros=true \
		-J-Dclojure.compiler.direct-linking=true \
		-H:ConfigurationFileDirectories=graal-configs/ \
		--initialize-at-build-time \
		-H:Log=registerResource: \
		-H:EnableURLProtocols=http,https \
		--verbose \
		--allow-incomplete-classpath \
		--no-fallback \
		--no-server \
		"-J-Xmx6g"

run: all
	build/mullion

test-bin: all
	build/mullion --test-load

package-linux: all
	-rm -rf build/linux-package
	-mkdir -p build/linux-package
	cp build/mullion build/linux-package
	cd build/linux-package && GZIP=-9 tar cvzf ../mullion-$(VERSION)-linux-amd64.tgz mullion
	cp target/uberjar/mullion-$(VERSION)-standalone.jar build/mullion-$(VERSION)-linux-amd64.jar
	du -sh build/mullion build/mullion-$(VERSION)-linux-amd64.tgz build/mullion-$(VERSION)-linux-amd64.jar

package-macos: all
	-rm -rf build/macos-package
	-mkdir -p build/macos-package
	cp build/mullion build/macos-package
	cd build/macos-package && zip ../mullion-$(VERSION)-macos-amd64.zip mullion
	cp target/uberjar/mullion-$(VERSION)-standalone.jar build/mullion-$(VERSION)-macos-amd64.jar
	du -sh build/mullion build/mullion-$(VERSION)-macos-amd64.zip build/mullion-$(VERSION)-macos-amd64.jar

src/cpp/jniMullion.h: src/cpp/Mullion.java src/cpp/mullion.cpp
	which java
	java -version
	which javac
	javac -version
	cd src/cpp && java -jar ~/.m2/repository/org/bytedeco/javacpp/1.5.4-SNAPSHOT/javacpp-1.5.4-SNAPSHOT.jar Mullion.java -header -nodelete

mullion: src/cpp/jniMullion.h
	#g++ -I/usr/lib/jvm/java-11-openjdk-amd64/include/ -I/usr/lib/jvm/java-11-openjdk-amd64/include/linux/ src/cpp/mullion.cpp src/cpp/linux-x86_64/libjniMullion.so -o mullion
	g++ -I$(JAVA_HOME)/include/ -I$(JAVA_HOME)/include/linux/ mullion.cpp src/cpp/linux-x86_64/libjniMullion.so -o mullion
