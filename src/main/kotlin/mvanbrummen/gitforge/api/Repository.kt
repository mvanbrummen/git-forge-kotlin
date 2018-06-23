package mvanbrummen.gitforge.api

import mvanbrummen.gitforge.util.GitRepositorySummary


data class RepositorySummary(val description: String, val repoSummary: GitRepositorySummary)