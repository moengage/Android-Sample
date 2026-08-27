package com.moengage.sampleapp.sdkhelper

import com.moengage.core.DataCenter
import org.junit.Assert.assertEquals
import org.junit.Test

class MoEngageDataCenterTest {

    @Test
    fun `dashboard form maps to the matching data centre`() {
        assertEquals(DataCenter.DATA_CENTER_1, dataCenterFrom("DATA_CENTER_1"))
        assertEquals(DataCenter.DATA_CENTER_3, dataCenterFrom("DATA_CENTER_3"))
        assertEquals(DataCenter.DATA_CENTER_6, dataCenterFrom("DATA_CENTER_6"))
        assertEquals(DataCenter.DATA_CENTER_101, dataCenterFrom("DATA_CENTER_101"))
    }

    @Test
    fun `sdk value form and bare digits map too`() {
        assertEquals(DataCenter.DATA_CENTER_2, dataCenterFrom("dc2"))
        assertEquals(DataCenter.DATA_CENTER_101, dataCenterFrom("DC101"))
        assertEquals(DataCenter.DATA_CENTER_4, dataCenterFrom("4"))
    }

    @Test
    fun `case and surrounding whitespace are ignored`() {
        assertEquals(DataCenter.DATA_CENTER_5, dataCenterFrom("  data_center_5  "))
    }

    @Test
    fun `unrecognised values fall back to data centre 1 instead of failing`() {
        assertEquals(DataCenter.DATA_CENTER_1, dataCenterFrom(""))
        assertEquals(DataCenter.DATA_CENTER_1, dataCenterFrom("DATA_CENTER_X"))
        assertEquals(DataCenter.DATA_CENTER_1, dataCenterFrom("DATA_CENTER_9"))
        assertEquals(DataCenter.DATA_CENTER_1, dataCenterFrom("eu"))
    }
}
