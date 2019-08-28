package com.shizy.utils.auth;

import com.twitter.snowflake.sequence.IdSequence;
import com.twitter.snowflake.support.IdSequenceFactory;

import java.util.UUID;

public class IdUtil {

    public static String genUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * unique id
     */
    public static String uniqueIdStr() {
        return String.valueOf(uniqueIdLong());
    }

    public static long uniqueIdLong() {
        return SequenceHolder.snowSequence.nextId();
    }

    private static class SequenceHolder {

        public static IdSequence snowSequence = null;

        static {
            IdSequenceFactory defaultFactory = new IdSequenceFactory();

            // set worker id
            defaultFactory.setWorkerId(1L);
            // 2018-03-05
            defaultFactory.setEpochMillis(1545187068690L);

            // create sequence
            snowSequence = defaultFactory.create();
        }

    }
}
