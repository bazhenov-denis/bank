package org.example.bank.importer;


import org.example.bank.domain.Operation;

import java.util.List;

public class JsonDataImporter extends DataImporter<Operation> {

    @Override
    protected List<Operation> parseData(String fileContent) {
        // Здесь можно использовать библиотеку Jackson или Gson для парсинга JSON
        return List.of();
    }
}
