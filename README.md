# BreakTag pipeline
Here we provide the tools to perform paired end or single read BreakTag raw data processing. The pipeline is also valid for (s)BLISS data. As input files you may use either gzipped fastq-files (.fastq.gz) or mapped read data (.bam files). In case of paired end reads, corresponding fastq files should be named using *.R1.fastq.gz* and *.R2.fastq.gz* suffixes. 
Some steps of the pipeline are based on the [blissNP pipeline](https://github.com/BiCroLab/blissNP) developed at the BriCo lab for BLISS data.

## Overall description

![breakinspectoR workflow](assets/breakinspectoR_wf.png)

### Initial preprocessing

Initial preprocessing is typically done in a linux cluster using the [Breaktag pipeline](https://github.com/roukoslab/breaktag). It includes the following steps:

1. scanning for reads (single- or paired-end) containing the expected 8-nt UMI followed by the 8-nt sample barcode in the 5' end of read 1.
2. alignment of reads to reference genome with BWA, with a seed length of 19 and default scoring/penalty values for mismatches, gaps and read clipping.
3. reads mapped with a minimum quality score Q (defaults to Q=60) are retained.
4. close spatial consecutive reads within a window of 30 nucleotides and UMI differing with up to 2 mismatches are considered PCR duplicates and only one is kept.
    
The resulting reads are aggregated per position and reported as a BED file.
    
### breakinspectoR analysis

This R package implements the identification of CRISPR (currently, Cas9 only) targets and estimation of the scission profile. Additionally it provides several plotting functions to graphically summarize the results.

## Installing the pipeline
### Docker installation
This repository contains a `Dockerfile` which can be used to create a Docker image with all dependencies.
This is the preferred and easiest way to have all the dependencies satisfied and run the pipeline.

### Manual installation
Install and make available in the path the following dependencies:
- Bpipe
- Bedtools
- BWA
- FastQC
- MultiQC
- Samtools
- Several unix standard tools (perl5, python3, awk, etc.)

## Running the pipeline
Tools are expected to be in the PATH. From the root of the folder where you clone the [breaktag pipeline](git@github.com:roukoslab/breaktag.git):

- edit the parameters file `breaktag/pipelines/breaktag/essential.vars.groovy`
- edit the targets file `breaktag/pipelines/breaktag/targets.txt`
- softlink these 2 files to the root folder `ln -s breaktag/pipelines/breaktag/essential.vars.groovy . && ln -s breaktag/pipelines/breaktag/targets.txt .`
- run the pipeline with this command (eg. from within the docker container):
```
bpipe run -n256 breaktag/pipelines/breaktag/breaktag.pipeline.groovy rawdata/*.fastq.gz
```

## Pipeline-specific parameter settings (files you need to setup in order to run the pipeline):
- `targets.txt`: tab-separated txt-file giving information about the analysed samples. The following columns are required
  - name: sample name. Experiment ID found in fastq filename: expID_R1.fastq.gz
  - pattern: UMI+barcode pattern file used in the linker

- essential.vars.groovy: essential parameters describing the experiment
  - ESSENTIAL_PROJECT: root folder of the analysis
  - ESSENTIAL_SAMPLE_PREFIX: sample name prefix to be trimmed in the results
  - ESSENTIAL_THREADS: number of threads for parallel tasks
  - ESSENTIAL_BWA_REF: path to bwa indexed reference genome
  - ESSENTIAL_PAIRED: either paired end ("yes") or single read ("no") design
  - ESSENTIAL_QUALITY: minimum mapping quality desired

### breaktag ESSENTIAL VARIABLES

Important parameters are included in this file. They're distributed in several sections.

#### General parameters
```
ESSENTIAL_PROJECT="/project/folder"
ESSENTIAL_SAMPLE_PREFIX="" 
ESSENTIAL_THREADS=16
```

#### Mapping parameters
```
ESSENTIAL_BWA_REF="/ref/index/bwa/hg38.fa"  // BWA index of the reference genome
ESSENTIAL_PAIRED="yes"        // paired end design
ESSENTIAL_QUALITY=60          // min mapping quality of reads to be kept. Defaults to 60 (discarding multimappers and low quality mapping)
```

#### Other
```
RUN_IN_PAIRED_END_MODE=(ESSENTIAL_PAIRED == "yes")
```

More fine-grained tunning of the tools called by the pipeline can be controled from the `.header` files in the `breaktag/modules` folder.

### Targets file

A tab-separated file with the filenames (excluding the .fastq.gz extension) and the breaktag barcode and the position of the UMI within the read.

```sh
name	pattern	umi
FANCF_rep1	^(........)(.TCACACGT|C.CACACGT|CT.ACACGT|CTC.CACGT|CTCA.ACGT|CTCAC.CGT|CTCACA.GT|CTCACAC.T|CTCACACG.)	$1
FANCF_rep2	^(........)(.TCACACGT|C.CACACGT|CT.ACACGT|CTC.CACGT|CTCA.ACGT|CTCAC.CGT|CTCACA.GT|CTCACAC.T|CTCACACG.)	$1
NT_rep1	^(........)(.TCACACGT|C.CACACGT|CT.ACACGT|CTC.CACGT|CTCA.ACGT|CTCAC.CGT|CTCACA.GT|CTCACAC.T|CTCACACG.)	$1
NT_rep2	^(........)(.TCACACGT|C.CACACGT|CT.ACACGT|CTC.CACGT|CTCA.ACGT|CTCAC.CGT|CTCACA.GT|CTCACAC.T|CTCACACG.)	$1
```

