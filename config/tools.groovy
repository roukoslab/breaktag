// This file defines data structures with all tools + versions + running_environments
// Names should match those of tools_envs
// NOTE: "default" runenv expects the tool to be in the PATH. Version is not really taken into account
tools_defaults = [
    bedtools: [ runenv: "default", version: "2.27"  ],
    bwa     : [ runenv: "default", version: "0.7.17"],
    fastqc  : [ runenv: "default", version: "0.11.9"],
    multiqc : [ runenv: "default", version: "1.9"   ],
    python  : [ runenv: "default", version: "3.6"   ],
    samtools: [ runenv: "default", version: "1.10"  ]
]

// This map defines how to prepare the environment in order to have in PATH all
// tools + versions + running_environments available. Structure:
//   * 1st level: tool name
//   * 2nd level: version --> version *string*
//   * 3rd level: runenv  --> one of [default]
// Then, the shell string which puts the tool in PATH. ":" means "do nothing, the tool is already in $PATH".
// Otherwise, the string could look like: "export PATH=$PATH:/opt/bwa/bin" or "module load bwa/0.7.17"
tools_envs = [
    bedtools: [
        "2.27": [
            default: ":"    // for instance, if bedtools is installed in `/opt/bedtools`, then replace ":" with "export PATH=$PATH:/opt/bedtools"
        ]
    ],
    bwa: [
        "0.7.17": [
            default: ":"
        ]
    ],
    fastqc: [
        "0.11.9": [
            default: ":"
        ]
    ],
    multiqc: [
        "1.9": [
            default: ":"
        ]
    ],
    python: [
        "3.6": [
            default: ":"
        ]
    ],
    samtools: [
        "1.10": [
            default: ":"
        ]
    ]
]

//
// prepare_tool_env
// 
// Given a tool, version and run env, return its tools_envs string.
// If any of the keys doesn't exist, returns the POSIX shell null command.
//
String prepare_tool_env (String tool, String version, String runenv) {

    if(tools_envs.containsKey(tool) &&
       tools_envs[tool].containsKey(version) &&
       tools_envs[tool][version].containsKey(runenv)) {
        return tools_envs[tool][version][runenv]
    } else {
        return ":"   // return the POSIX shell null command
    }
}

