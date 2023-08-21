# Identity Management Credential Services
MASTER:
[![build status](https://repo.aticloud.aero/identitymanagement/immigration-corridor/dtc-demonstrator/credential-services/badges/master/pipeline.svg)](https://repo.aticloud.aero/identitymanagement/immigration-corridor/dtc-demonstrator/credential-services/commits/master)
[![coverage report](https://repo.aticloud.aero/identitymanagement/immigration-corridor/dtc-demonstrator/credential-services/badges/master/coverage.svg)](https://repo.aticloud.aero/identitymanagement/immigration-corridor/dtc-demonstrator/credential-services/commits/master)
DEVELOP:
[![build status](https://repo.aticloud.aero/identitymanagement/immigration-corridor/dtc-demonstrator/credential-services/badges/develop/pipeline.svg)](https://repo.aticloud.aero/identitymanagement/immigration-corridor/dtc-demonstrator/credential-services/commits/develop)
[![coverage report](https://repo.aticloud.aero/identitymanagement/immigration-corridor/dtc-demonstrator/credential-services/badges/develop/coverage.svg)](https://repo.aticloud.aero/identitymanagement/immigration-corridor/dtc-demonstrator/credential-services/commits/develop)

### Description
This SpringBoot project provides the Credentials Service. 

Schemas and Credential definitions can be created and Verifiable Credentials can be issued and verified.

### CI/CD
## Install Docker and docker-compose on the AWS Instance
Docker
https://docs.aws.amazon.com/AmazonECS/latest/developerguide/docker-basics.html [https://docs.aws.amazon.com/AmazonECS/latest/developerguide/docker-basics.html]

docker-compose
https://stackoverflow.com/questions/63708035/installing-docker-compose-on-amazon-ec2-linux-2-9kb-docker-compose-file [https://stackoverflow.com/questions/63708035/installing-docker-compose-on-amazon-ec2-linux-2-9kb-docker-compose-file]

## Allow SSH access 
On the AWS instances (Sydney, Ohio and London) allow the GitLab runner (3.68.43.174) SSH access in the security group settings.

# GitLab CI/CD
CI/CD variable settings for the GitLab project 
```
For the Sydney region instance:
update CI_DEV_PEM variable with SSH .pem key
update CI_DEv variable with AWS EC2 instance IP address or domain name

For the Ohio region instance:
update CI_STAGING_PEM variable with SSH .pem key
update CI_STAGING variable with AWS EC2 instance IP address or domain name

For the London region instance:
update CI_PRODUCTION_PEM variable with SSH .pem key
update CI_PRODUCTION variable with AWS EC2 instance IP address or domain name
```

