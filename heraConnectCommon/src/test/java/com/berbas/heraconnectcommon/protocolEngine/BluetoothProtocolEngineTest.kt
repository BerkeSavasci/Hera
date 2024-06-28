package com.berbas.heraconnectcommon.protocolEngine


import com.berbas.heraconnectcommon.localData.person.Person
import com.berbas.heraconnectcommon.localData.sensor.FitnessData
import org.junit.Assert.assertEquals
import org.junit.Test

class BluetoothProtocolEngineTest {

    private val bluetoothProtocolEngine = BluetoothProtocolEngine()

    @Test
    fun `toPerson should return correct Person object`() {
        val data =
            "Person(" +
                    "firstname=John, " +
                    "lastname=Doe, " +
                    "birthday=1990-01-01, " +
                    "gender=Male, height=180, " +
                    "weight=80.0, stepGoal=10000, " +
                    "activityGoal=60.0" +
                    ")" +
            "*FitnessData(" +
                    "steps=[10000], " +
                    "bpm=[80], " +
                    "sleepTime=[8], " +
                    "initialStepCount=0, " +
                    "cumulativeSteps=10000" +
                    ")"

        val result = bluetoothProtocolEngine.toPerson(data)

        val expected = Person(
            firstname = "John",
            lastname = "Doe",
            birthday = "1990-01-01",
            gender = "Male",
            height = 180,
            weight = 80.0,
            stepGoal = 10000,
            activityGoal = 60.0
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toFitnessData should return correct FitnessData object`() {
        val data =
            "Person(" +
                    "firstname=John, " +
                    "lastname=Doe, " +
                    "birthday=1990.01.01, " +
                    "gender=Male, height=180, " +
                    "weight=80.0, stepGoal=10000, " +
                    "activityGoal=60.0" +
                    ")" +
            "*FitnessData(" +
                    "steps=[20.06.2024: 5000, 21.06.2024: 8500, 22.06.2024: 4030, 23.06.2024: 5687, 24.06.2024: 14063, 25.06.2024: 7765, 26.06.2024: 12038, 27.06.2024: 3890, 28.06.2024: 4783]," +
                    "bpm=[80, 70, 65, 85, 154], " +
                    "sleepTime=[8, 7.4, 5.3], " +
                    "initialStepCount=0, " +
                    "cumulativeSteps=10000" +
                    ")"

        val result = bluetoothProtocolEngine.toFitnessData(data)

        val expected = FitnessData(
            steps = arrayListOf(
                "20.06.2024: 5000",
                "21.06.2024: 8500",
                "22.06.2024: 4030",
                "23.06.2024: 5687",
                "24.06.2024: 14063",
                "25.06.2024: 7765",
                "26.06.2024: 12038",
                "27.06.2024: 3890",
                "28.06.2024: 4783"
            ),
            bpm = arrayListOf("80", "70", "65", "85", "154"),
            sleepTime = arrayListOf("8", "7.4", "5.3"),
            initialStepCount = 0,
            cumulativeSteps = 10000
        )
        assertEquals(expected, result)
    }
}