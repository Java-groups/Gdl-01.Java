FROM openjdk:8-jdk-alpine
COPY  "./target/sport-hub.jar" "sport-hub-docker-ex.jar"
EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "-Dbucket_name='sport-hub-11400.appspot.com' -Demail_pwd='12345' -Demail_username='jjcastellanosguzman@outlook.es' -Dproject_id=sport-hub-11400 -Dservice_account='D:\IT Academy\Couse Docs\sport-hub-11400-firebase-adminsdk-pxvmd-2bc8df8b47.json' -Duser_db=postgres -Duser_pwd=cGJZ@z3#B@xq9JM", "sport-hub-docker-ex.jar"]
ENTRYPOINT ["java", "-jar", "/sport-hub-docker-ex.jar"]