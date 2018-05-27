package mvanbrummen.gitforge.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Account(
    @Id
    val id: UUID,

    @Column
    val username: String,

    @Column
    val emailAddress: String,

    @Column
    val password: String
)

