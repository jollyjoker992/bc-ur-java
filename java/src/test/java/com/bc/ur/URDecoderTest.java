package com.bc.ur;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(JUnit4.class)
public class URDecoderTest {

    @Test
    public void testDecodeSinglePart() {
        UR expectedUR = UR.create(50, "Wolf");
        String encoded =
                "ur:bytes/hdeymejtswhhylkepmykhhtsytsnoyoyaxaedsuttydmmhhpktpmsrjtgwdpfnsboxgwlbaawzuefywkdplrsrjynbvygabwjldapfcsdwkbrkch";
        UR ur = URDecoder.decode(encoded);
        assertEquals(expectedUR.getType(), ur.getType());
        assertTrue(Arrays.deepEquals(TestUtils.toTypedArray(expectedUR.getCbor()), TestUtils.toTypedArray(ur.getCbor())));
    }

    @Test
    public void testDecodeMultiParts() {
        UR ur = UR.create(32767, "Wolf");
        UREncoder encoder = new UREncoder(ur, 1000, 100, 10);
        URDecoder decoder = new URDecoder();
        do {
            String part = encoder.nextPart();
            decoder.receivePart(part);
        } while (!decoder.isComplete());

        if (decoder.isSuccess()) {
            UR resultUR = decoder.resultUR();
            assertEquals(ur.getType(), resultUR.getType());
            assertTrue(Arrays.deepEquals(TestUtils.toTypedArray(ur.getCbor()), TestUtils.toTypedArray(resultUR.getCbor())));
        } else {
            throw decoder.resultError();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testDecodeError() {
        String[] invalidData = new String[]{
                "",
                "ur:bytes/",
                "ur:ur:ur",
                "uf:bytes/hdeymejtswhhylkepmykhhtsytsnoyoyaxaedsuttydmmhhpktpmsrjtgwdpfnsboxgwlbaawzuefywkdplrsrjynbvygabwjldapfcsdwkbrkch"
        };
        for (String it : invalidData) {
            URDecoder.decode(it);
        }
    }
}