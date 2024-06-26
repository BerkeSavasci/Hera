package com.berbas.fittrackapp.screens.profile

import com.berbas.heraconnectcommon.localData.person.Person
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var fakeRepository: FakePersonRepository


    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepository = FakePersonRepository()
        viewModel = ProfileViewModel(fakeRepository, 101)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `happy case test initial person state`() = runTest {
        val person = Person(
            id = 101,
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0
        )
        fakeRepository.upsertPerson(person)

        viewModel.observeAndRefreshPerson()

        val state = viewModel.state.value
        assertEquals(person.firstname, state.firstName)
        assertEquals(person.lastname, state.lastName)
        assertEquals(person.gender, state.gender)
        assertEquals(person.birthday, state.birthday)
        assertEquals(person.height, state.height)
        assertEquals(person.weight, state.weight, 0.0)
        assertEquals(person.stepGoal, state.stepGoal)
        assertEquals(person.activityGoal, state.activityGoal, 0.0)
    }

    @Test
    fun `happy case test person state after SetFirstName event`() = runTest {
        val person = Person(
            id = 101,
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0
        )
        fakeRepository.upsertPerson(person)

        viewModel.observeAndRefreshPerson()

        viewModel.onEvent(PersonEvent.SetFirstName("John"))

        val state = viewModel.state.value
        assertEquals("John", state.firstName)
    }

    @Test
    fun `happy case test person state after SetLastName event`() = runTest {
        val person = Person(
            id = 101,
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0
        )
        fakeRepository.upsertPerson(person)

        viewModel.observeAndRefreshPerson()

        viewModel.onEvent(PersonEvent.SetLastName("Doe"))

        val state = viewModel.state.value
        assertEquals("Doe", state.lastName)
    }

    @Test
    fun `happy case test person state after SetGender event`() = runTest {
        val person = Person(
            id = 101,
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0
        )
        fakeRepository.upsertPerson(person)

        viewModel.observeAndRefreshPerson()

        viewModel.onEvent(PersonEvent.SetGender("female"))

        val state = viewModel.state.value
        assertEquals("female", state.gender)
    }

    @Test
    fun `happy case test person state after SetBirthday event`() = runTest {
        val person = Person(
            id = 101,
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0
        )
        fakeRepository.upsertPerson(person)

        viewModel.observeAndRefreshPerson()

        viewModel.onEvent(PersonEvent.SetBirthday("02/02/2002"))

        val state = viewModel.state.value
        assertEquals("02/02/2002", state.birthday)
    }

    @Test
    fun `happy case test person state after SetHeight event`() = runTest {
        val person = Person(
            id = 101,
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0
        )
        fakeRepository.upsertPerson(person)

        viewModel.observeAndRefreshPerson()

        viewModel.onEvent(PersonEvent.SetHeight(175))

        val state = viewModel.state.value
        assertEquals(175, state.height)
    }

    @Test
    fun `happy case test person state after SetWeight event`() = runTest {
        val person = Person(
            id = 101,
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0
        )
        fakeRepository.upsertPerson(person)

        viewModel.observeAndRefreshPerson()

        viewModel.onEvent(PersonEvent.SetWeight(89.2))

        val state = viewModel.state.value
        assertEquals(89.2, state.weight)
    }

    @Test
    fun `happy case test person state after SetStepGoal event`() = runTest {
        val person = Person(
            id = 101,
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0
        )
        fakeRepository.upsertPerson(person)

        viewModel.observeAndRefreshPerson()

        viewModel.onEvent(PersonEvent.SetStepGoal(8500))

        val state = viewModel.state.value
        assertEquals(8500, state.stepGoal)
    }

    @Test
    fun `happy case test person state after SetActivityGoal event`() = runTest {
        val person = Person(
            id = 101,
            firstname = "Max",
            lastname = "Mustermann",
            gender = "male",
            birthday = "01/01/2000",
            height = 180,
            weight = 80.0,
            stepGoal = 6000,
            activityGoal = 2.0
        )
        fakeRepository.upsertPerson(person)

        viewModel.observeAndRefreshPerson()

        viewModel.onEvent(PersonEvent.SetActivityGoal(2.5))

        val state = viewModel.state.value
        assertEquals(2.5, state.activityGoal)
    }

    @Test
    fun `test initial person state when person is null`() = runTest {
        viewModel.observeAndRefreshPerson()

        val state = viewModel.state.value
        assertEquals("Placeholder", state.firstName)
        assertEquals("Placeholder", state.lastName)
        assertEquals("Placeholder", state.gender)
        assertEquals("Placeholder", state.birthday)
        assertEquals(1, state.height)
        assertEquals(1.1, state.weight, 0.0)
        assertEquals(6000, state.stepGoal)
        assertEquals(1.5, state.activityGoal, 0.0)
    }

    @Test
    fun `test person state after SetFirstName event with blank string`() = runTest {
        viewModel.onEvent(PersonEvent.SetFirstName("   "))

        val state = viewModel.state.value
        assertNotEquals("   ", state.firstName)
    }

    @Test
    fun `test person state after SetLastName event with blank string`() = runTest {
        viewModel.onEvent(PersonEvent.SetLastName("   "))

        val state = viewModel.state.value
        assertNotEquals("   ", state.lastName)
    }

    @Test
    fun `test person state after SetGender event with blank string`() = runTest {
        viewModel.onEvent(PersonEvent.SetGender("   "))

        val state = viewModel.state.value
        assertNotEquals("   ", state.gender)
    }

    @Test
    fun `test person state after SetBirthday event with blank string`() = runTest {
        viewModel.onEvent(PersonEvent.SetBirthday("   "))

        val state = viewModel.state.value
        assertNotEquals("   ", state.birthday)
    }

    @Test
    fun `test person state after SetHeight event with zero value`() = runTest {
        viewModel.onEvent(PersonEvent.SetHeight(0))

        val state = viewModel.state.value
        assertNotEquals(0, state.height)
    }

    @Test
    fun `test person state after SetWeight event with zero value`() = runTest {
        viewModel.onEvent(PersonEvent.SetWeight(0.0))

        val state = viewModel.state.value
        assertNotEquals(0.0, state.weight)
    }

    @Test
    fun `test person state after SetStepGoal event with zero value`() = runTest {
        viewModel.onEvent(PersonEvent.SetStepGoal(0))

        val state = viewModel.state.value
        assertNotEquals(0, state.stepGoal)
    }

    @Test
    fun `test person state after SetActivityGoal event with zero value`() = runTest {
        viewModel.onEvent(PersonEvent.SetActivityGoal(0.0))

        val state = viewModel.state.value
        assertNotEquals(0.0, state.activityGoal)
    }
}