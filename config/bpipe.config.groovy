// Job resource limits. Adjust to your needs, though defaults are usually good enough.
// NOTES:
//    * the commands are *sorted*. Please, keep the order!!!
//    * check the documentation here: http://docs.bpipe.org/Language/Config/
//
config {
  commands {
    BWA_pe {
      walltime="24:00:00"
      procs="16"
      memory="32"
    }
    collectBpipeLogs {
      walltime="00:45:00"
      procs="1"
      memory="64"
    }
    count_breaks {
      walltime="04:00:00"
      procs="4"
      memory="16"
    }
    count_breaks_strandless {
      walltime="01:00:00"
      procs="1"
      memory="1"
    }
    FastQC {
      walltime="02:00:00"
      procs="1"
      memory="2"
    }
    MULTIQC {
      walltime="12:00:00"
      procs="4"
      memory="128"
    }
    pattern_filtering {
      walltime="04:00:00"
      procs="2"
      memory="1"
    }
    umidedup {
      walltime="05:00:00"
      procs="1"
      memory="5"
    }
  }
}
