#!/usr/bin/env python3

import os
import sys
import subprocess

def compile_java():
    """Compile Java source files and create JAR archive"""
    # Create Makefile
    with open("Makefile", "w") as f:
        f.write("""
.PHONY: all compile run clean jar

all: compile

compile:
	javac -d . arbitraryarithmetic/AInteger.java arbitraryarithmetic/AFloat.java
	javac -d . -cp . MyInfArith.java

run:
	java -cp . MyInfArith $(ARGS)

jar:
	jar cf aarithmetic.jar arbitraryarithmetic/*.class

clean:
	rm -rf arbitraryarithmetic/*.class MyInfArith.class aarithmetic.jar
        """)

    # Compile Java code
    try:
        subprocess.run(["javac", "-d", ".", "arbitraryarithmetic/AInteger.java", "arbitraryarithmetic/AFloat.java"],
                       check=True,
                       shell=True)
        subprocess.run(["javac", "-d", ".", "-cp", ".", "MyInfArith.java"], check=True)

        # Create JAR file
        subprocess.run(["jar", "cf", "aarithmetic.jar", "arbitraryarithmetic/*.class"], check=True, shell=True)

        print("Compilation completed successfully.")
    except subprocess.CalledProcessError:
        print("Compilation failed. Please check the source code.")


def run_java(args):
    """Run the Java application with the provided arguments"""
    try:
        result = subprocess.run(["java", "-cp", ".", "MyInfArith"] + args, capture_output=True, text=True)
        print(result.stdout)
        if result.stderr:
            print("Error:", result.stderr)
    except Exception as e:
        print(f"Error running Java application: {e}")


if __name__ == "__main__":
    if len(sys.argv) == 1:
        # No arguments, compile only
        compile_java()
    else:
        # Have arguments, run the program
        run_java(sys.argv[1:])
