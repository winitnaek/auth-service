/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author igorV
 */
public class LogUtils {

    private final static Logger log = LoggerFactory.getLogger(LogUtils.class);

    /**
     * <p>
     * Converts vararg array of name-value pairs to JSON string.
     * </p>
     *
     * @param msg
     * @param pairs
     * @return
     */
    public static String jsonize(Object... valsIn) {
        try {
            Pair pair = new Pair();
            List<Object> vals = Arrays.asList(valsIn);
            List<Pair> pairs = new ArrayList<Pair>(vals.size() / 2);

            for (Object val : vals) {
                int idx = vals.indexOf(val);

                if ((idx + 1) % 2 == 1) {
                    pair.setKey(StringUtils.trimToEmpty(val != null ? val
                            .toString() : null));
                } else {
                    pair.setValue(StringUtils.trimToEmpty(val != null ? val
                            .toString() : null));
                    pairs.add(pair);
                    pair = new Pair();
                }
            }

            StringBuilder msgOut = new StringBuilder();
            ObjectMapper mapper = new ObjectMapper();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            mapper.writeValue(out, pairs);
            msgOut.append(new String(out.toByteArray()));
            String msgToRet = msgOut.toString();

            if (log.isTraceEnabled()) {
                log.trace("Message as JSON -> " + msgToRet);
            }

            return msgToRet;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(LogUtils.jsonize(
                        "msg", "Failed to convert to JSON string! " + e.getMessage()), e);
            }

            return "";
        }
    }

    /**
     * @author igorv
     *
     */
    private static class Pair {

        @JsonProperty
        private String key;
        @JsonProperty
        private Object value;

        /**
         * @return the value
         */
        public Object getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key the key to set
         */
        public void setKey(String key) {
            this.key = key;
        }

        /* (non-Javadoc)
		 * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
