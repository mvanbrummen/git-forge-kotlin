package mvanbrummen.gitforge.domain

import java.util.*
import javax.persistence.*

@Entity
data class Repository(
    @Id
    val id: UUID,

    @ManyToOne
    val account: Account,

    @Column
    val name: String,

    @Column
    val description: String?
)

