FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye

ARG INSTALL_MAVEN="true"
ARG MAVEN_VERSION=""

ARG INSTALL_GRADLE="false"
ARG GRADLE_VERSION=""

RUN if [ "${INSTALL_MAVEN}" = "true" ]; then su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""; fi \
    && if [ "${INSTALL_GRADLE}" = "true" ]; then su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install gradle \"${GRADLE_VERSION}\""; fi

# [Optional] Uncomment this section to install additional OS packages.
# RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
#     && apt-get -y install --no-install-recommends <your-package-list-here>

# Install k6
RUN wget https://github.com/grafana/k6/releases/download/v1.0.0-rc1/k6-v1.0.0-rc1-linux-arm64.tar.gz
RUN tar -xvzf k6-v1.0.0-rc1-linux-arm64.tar.gz
RUN mv k6-v1.0.0-rc1-linux-arm64/k6 /usr/local/bin/
RUN chmod 777 /usr/local/bin/k6
# [Optional] Uncomment this line to install global node packages.
# RUN su vscode -c "source /usr/local/share/nvm/nvm.sh && npm install -g <your-package-here>" 2>&1

USER vscode
WORKDIR /home/vscode
RUN wget https://raw.githubusercontent.com/kitakou0313/dotfiles/main/installer.sh && chmod 777 installer.sh
RUN bash installer.sh

ENV TZ="Asia/Tokyo"
