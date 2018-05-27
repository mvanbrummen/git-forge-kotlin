package mvanbrummen.gitforge.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class RepositoryRepositoryTest {

    @Autowired
    lateinit var repositoryRepository: RepositoryRepository

    @Test
    fun testGet() {
        val result = repositoryRepository.findByAccount_Username("mvanbrummen")

        assertThat(result).isNotNull
    }
}