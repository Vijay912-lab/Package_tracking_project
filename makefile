JAVAC = javac
JAR = jar
SRC_DIR = src
BIN_DIR = bin
JAR_NAME = PackageTracking.jar
MAIN_CLASS = ui.LandingPage

# Find all Java source files recursively
SOURCES := $(shell find $(SRC_DIR) -name "*.java")

# Default target
all: $(JAR_NAME)

# Compile all .java files into bin/
$(JAR_NAME):
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SOURCES)
	$(JAR) cfm $(JAR_NAME) manifest.txt -C $(BIN_DIR) .

# Clean all generated files
clean:
	rm -rf $(BIN_DIR) $(JAR_NAME)

