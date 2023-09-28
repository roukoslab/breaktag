FROM condaforge/mambaforge:latest

LABEL org.opencontainers.image.authors="roukoslab" \
      org.opencontainers.image.title="breaktag" \
      org.opencontainers.image.version="1.0"

USER root

RUN mamba install -y bioconda::multiqc 
RUN mamba install -y bioconda::bpipe
RUN mamba install -y bioconda::bwa
RUN mamba install -y bioconda::fastqc
RUN mamba install -y bioconda::samtools
RUN mamba install -y bioconda::bedtools

RUN apt update -y \
    && apt install -y --no-install-recommends python3 python3-numpy \
    && apt install -y --no-install-recommends cpanminus \
    && cpanm Getopt::Std

USER $MAMBA_USER

