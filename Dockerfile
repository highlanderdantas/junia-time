FROM adoptopenjdk/openjdk13:alpine-nightly
LABEL Highlander Dantas <haylander60@gmail.com>
VOLUME /tmp
ENV PORT=8880
ENV MONGOURL=${MONGOURL}
ENV WEBHOOK=${WEBHOOK}
ADD target/junia-time.jar junia-time.jar
EXPOSE $PORT
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseShenandoahGC", "-jar",  "junia-time.jar"]