package org.bsdevelopment.mobfarming.blocks.entity.base;

import org.bsdevelopment.mobfarming.utilities.storage.BulkStorage;

public interface IStorage {
    BulkStorage getBulkStorage();

    void saveStorage();
}
