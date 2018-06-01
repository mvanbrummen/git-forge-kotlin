package mvanbrummen.gitforge.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
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
    @Ignore
    fun `get account by username should return list of repos`() {
        val res = repositoryRepository.findRepositoriesByAccount("mvanbrummen")

        assertThat(res).isNotNull
        assertThat(res).isNotEmpty
    }

    @Test
    fun `get account by unknown username should return empty list`() {
        val res = repositoryRepository.findRepositoriesByAccount("unknown")

        assertThat(res).isEmpty()
    }
}