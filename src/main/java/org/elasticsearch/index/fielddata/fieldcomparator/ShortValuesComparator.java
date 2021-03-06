/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.fielddata.fieldcomparator;

import org.elasticsearch.index.fielddata.IndexNumericFieldData;

import java.io.IOException;

/**
 */
public final class ShortValuesComparator extends LongValuesComparatorBase<Short> {

    private final short[] values;
    private final SortMode sortMode;

    public ShortValuesComparator(IndexNumericFieldData<?> indexFieldData, short missingValue, int numHits, SortMode sortMode) {
        super(indexFieldData, missingValue, sortMode);
        assert indexFieldData.getNumericType().requiredBits() <= 16;
        this.values = new short[numHits];
        this.sortMode = sortMode;
    }

    @Override
    public int compare(int slot1, int slot2) {
        final int v1 = values[slot1];
        final int v2 = values[slot2];
        return v1 - v2; // we cast to int so it can't overflow
    }

    @Override
    public void setBottom(int slot) {
        this.bottom = values[slot];
    }

    @Override
    public void copy(int slot, int doc) throws IOException {
        values[slot] = (short) readerValues.getValueMissing(doc, missingValue);
    }

    @Override
    public Short value(int slot) {
        return Short.valueOf(values[slot]);
    }

    @Override
    public void add(int slot, int doc) {
        values[slot] += (short) readerValues.getValueMissing(doc, missingValue);
    }

    @Override
    public void divide(int slot, int divisor) {
        values[slot] /= divisor;
    }

    @Override
    public void missing(int slot) {
        values[slot] = (short) missingValue;
    }
}
