import com.jigcar.stepfunction.util.isValidUuid
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UuidValidatorTest {

    @Test
    fun `valid UUID returns true`() {
        val uuid = "123e4567-e89b-12d3-a456-426614174000"
        assertTrue(isValidUuid(uuid))
    }

    @Test
    fun `invalid UUID returns false - wrong format`() {
        val invalidUuid = "not-a-uuid"
        assertFalse(isValidUuid(invalidUuid))
    }

    @Test
    fun `invalid UUID returns false - missing dashes`() {
        val invalidUuid = "123e4567e89b12d3a456426614174000"
        assertFalse(isValidUuid(invalidUuid))
    }

    @Test
    fun `invalid UUID returns false - empty string`() {
        val invalidUuid = ""
        assertFalse(isValidUuid(invalidUuid))
    }

    @Test
    fun `invalid UUID returns false - null-like string`() {
        val invalidUuid = "null"
        assertFalse(isValidUuid(invalidUuid))
    }
}
