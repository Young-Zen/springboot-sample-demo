exec java -cp app:app/lib/* -server -XX:+HeapDumpOnOutOfMemoryError -XX:+UnlockExperimentalVMOptions ${ENABLE_DEBUG:+'-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9090'} $JAVA_OPTS $*