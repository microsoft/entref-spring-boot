# Use this Dockerfile to upload the sample data without needing to install the dependencies locally in your system.
# - build the docker image: docker build .
# - run  the docker image: docker run -it <image id from previous step>

FROM ubuntu:xenial

# Update and Isntall Dependencies
RUN apt-get update && \
    apt-get install apt-transport-https lsb-release software-properties-common dirmngr git mongodb curl -y

# Azure deps
RUN AZ_REPO=$(lsb_release -cs) && \
    echo "deb [arch=amd64] https://packages.microsoft.com/repos/azure-cli/ $AZ_REPO main" | \
    tee /etc/apt/sources.list.d/azure-cli.list
RUN apt-key --keyring /etc/apt/trusted.gpg.d/Microsoft.gpg adv \
    --keyserver packages.microsoft.com \
    --recv-keys BC528686B50D79E339D3721CEB3E94ADBE1229CF

# Mongo deps
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 9DA31620334BD75D9DCB49F368818C72E52529D4
RUN echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/4.0 multiverse" | \
    tee /etc/apt/sources.list.d/mongodb-org-4.0.list

# Update + Install Mongo and Azure CLI tools.
RUN apt-get update
RUN apt-get install azure-cli mongodb-org-tools -y

# Clone Code Repo
RUN git clone https://github.com/Microsoft/containers-rest-cosmos-appservice-java.git
WORKDIR /containers-rest-cosmos-appservice-java/data
