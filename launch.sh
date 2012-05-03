#!/bin/bash

java -Xmx4096m -classpath .:bin/:./lib/imgscalr-lib-4.2.jar:./lib/bing-search-java-sdk.jar:./lib/gson-1.4.jar mosaicmaker.Main ./test_images/testImage2.jpg ./testOutput.png ./test_images/dir_test/ 200 100
