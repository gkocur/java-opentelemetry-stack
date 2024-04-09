FROM --platform=$BUILDPLATFORM  gradle:jdk17 as build
WORKDIR /app
COPY . .
RUN gradle assemble

FROM --platform=$BUILDPLATFORM  amazoncorretto:17-alpine3.19
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
CMD ["java", "-jar", "app.jar"]