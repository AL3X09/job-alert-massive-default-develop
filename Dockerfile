FROM 10.129.146.30:8081/devops-ath:openjdk-21-jre-slim-df-test
USER txusr
RUN cd / && sudo mkdir -p data && sudo chown -R txusr: data
RUN cd /data && sudo mkdir logs && sudo chown -R txusr: logs 
RUN cd /data/logs && sudo mkdir admin_alertas && sudo chown -R txusr: admin_alertas
RUN cd / && sudo chown -R txusr: /usr
EXPOSE 31795
ADD target/job-alert-massive-default-0.0.1-SNAPSHOT.jar app.jar