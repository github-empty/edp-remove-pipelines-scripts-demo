import jenkins.model.Jenkins

Jenkins jenkins = Jenkins.instance

String repository = "https://github.com/github-empty/edp-remove-pipelines-scripts"

String removePipelinesFolderName = "remove-pipelines"
def removePipelinesFolder = jenkins.getItem(removePipelinesFolderName)
if (removePipelinesFolder == null) {
    folder(removePipelinesFolderName)
}

// Remove cd stage pipeline
pipelineJob("${removePipelinesFolderName}/remove-cd-stage") {
    logRotator {
        numToKeep(10)
    }
    definition {
        cpsScm {
            lightweight = true
            scm {
                git {
                    remote {
                        url(repository)
                    }
                    branches("master")
                    scriptPath("remove-cd-stage.groovy")
                }
            }
            parameters {
                booleanParam('INTERACTIVE_MODE', true, 'If set to true, fields CD_PIPELINE and CD_STAGE ' +
                        'will be ignored and these parameters will be selected from the dropdown list later.')
                stringParam('CD_PIPELINE', '', 'CD pipeline you want to remove stage in.')
                stringParam('CD_STAGE', '', 'CD stage you want to remove.')
                booleanParam('RESOURCES_VERSION_2', true, 'Use custom resources version 2 if set to true.')
            }
        }
    }
}

// remove cd pipeline pipeline
pipelineJob("${removePipelinesFolderName}/remove-cd-pipeline") {
    logRotator {
        numToKeep(10)
    }
    definition {
        cpsScm {
            lightweight = true
            scm {
                git {
                    remote {
                        url(repository)
                    }
                    branches("master")
                    scriptPath("remove-cd-pipeline.groovy")
                }
            }
            parameters {
                booleanParam('INTERACTIVE_MODE', true, 'If set to true, field CD_PIPELINE' +
                        'will be ignored and this parameter will be selected from the dropdown list later.')
                stringParam('CD_PIPELINE', '', 'CD pipeline you want to remove.')
                booleanParam('RESOURCES_VERSION_2', true, 'Use custom resources version 2 if set to true.')
            }
        }
    }
}

// remove codebase branch pipeline
pipelineJob("${removePipelinesFolderName}/remove-codebase-branch") {
    logRotator {
        numToKeep(10)
    }
    definition {
        cpsScm {
            lightweight = true
            scm {
                git {
                    remote {
                        url(repository)
                    }
                    branches("master")
                    scriptPath("remove-codebase-branch.groovy")
                }
            }
            parameters {
                booleanParam('INTERACTIVE_MODE', true, 'If set to true, fields CODEBASE and CODEBASE_BRANCH ' +
                        'will be ignored and these parameters will be selected from the dropdown list later.')
                stringParam('CODEBASE', '', 'Codebase you want to remove branch in.')
                stringParam('CODEBASE_BRANCH', '', 'Codebase branch you want to remove. Branch \"master\" is protected from removing and ' +
                        'can be removed only with the whole codebase.')
                booleanParam('RESOURCES_VERSION_2', true, 'Use custom resources version 2.')
                booleanParam('REMOVE_BRANCH_SONAR_PROJECT', true, 'Remove ${CODEBASE}:${CODEBASE_BRANCH} sonar project. This parameter is ignored ' +
                        'for autotests codebases.')
            }
        }
    }
}

// remove codebase pipeline
pipelineJob("${removePipelinesFolderName}/remove-codebase") {
    logRotator {
        numToKeep(10)
    }
    definition {
        cpsScm {
            lightweight = true
            scm {
                git {
                    remote {
                        url(repository)
                    }
                    branches("master")
                    scriptPath("remove-codebase.groovy")
                }
            }
            parameters {
                booleanParam('INTERACTIVE_MODE', true, 'If set to true, field CODEBASE ' +
                        'will be ignored and this parameters will be selected from the dropdown list later.')
                stringParam('CODEBASE', '', 'Codebase you want to remove.')
                booleanParam('RESOURCES_VERSION_2', true, 'Use custom resources version 2 if set to true.')
                booleanParam('REMOVE_GERRIT_PROJECT', true, 'Gerrit project will be removed if set to true.')
                stringParam('GERRIT_SSH_PORT', '', 'Required if REMOVE_GERRIT_PROJECT is true.')
                booleanParam('REMOVE_SONAR_PROJECTS', true, 'Remove all sonar projects related to codebase.')
            }
        }
    }
}
