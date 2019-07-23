# netifi-streaming-example

### Getting started

0. authenticate with gcloud, select correct k8s project (netifi-test)

1. Redeploy acme application on k8s (needs kubectl) 
`scripts/k8s`: `./teardown-acme.sh`, `./setup-acme.sh`

2. Redeploy jmeter client (manual step)  
   ssh into `acme-runner` VM instance on gcp  
   stop existing container, deploy new by pasting contents of `runner/start-runner.sh`  
    
3. Redeploy jmeter servers (manual step (actually automated, but code is not up-to-date))  
   * ssh into each node with `acmeairpg-jmeter-node` (usually rescale to 1 for experiments)  
   * stop existing container, deploy new by pasting contents of `jmeter-driver/start-jmeter-driver.sh`  
4. At this point acme + loadtest is ready. One needs means to ask load test to start, and receive results - `runner-client`.  
   It is started by load test entrypoint:`./start-runner-client.sh`
5. Some time after test is started, prometheus at `http://35.232.11.90/graph` will have the data of acme application

#### Starting jmeter client
JVM_ARGS="-Duser.timezone=America/Los_Angeles -Xmx8g -Xms8g -XX:+AlwaysPreTouch -XX:+UseStringDeduplication -XX:+UseG1GC" ./jmeter -n -t testplan.jmx