package ca.dal.database.storage.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Harsh Shah
 */
public class GlobalDataDictionary {

    List<DataDictionaryEntry> entries;

    public GlobalDataDictionary(List<DataDictionaryEntry> entries) {
        this.entries = entries;
    }

    public List<DataDictionaryEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<DataDictionaryEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(DataDictionaryEntry entry) {
        if(entries.isEmpty()){
            this.entries = new ArrayList<>();
        }

        this.entries.add(entry);
    }

    public static GlobalDataDictionary parse(List<String> lines){
        List<DataDictionaryEntry> entries = new ArrayList<>();
        for(String line: lines){
            entries.add(DataDictionaryEntry.parse(line));
        }

        return new GlobalDataDictionary(entries);
    }

    public List<String> toListString() {
        return entries.stream().map(DataDictionaryEntry::toString).collect(Collectors.toList());
    }

    public Set<String> getInstancesInvolved(String databaseName){
        return entries.stream().filter(itr -> itr.getDatabaseName().equals(databaseName))
                .map(DataDictionaryEntry::getInstanceName).collect(Collectors.toSet());
    }

    public Optional<DataDictionaryEntry> getInstanceInvolved(String databaseName, String tableName){
        return entries.stream().filter(itr -> itr.getDatabaseName().equals(databaseName) && itr.getTableName().equals(tableName)).findFirst();
    }

    @Override
    public String toString() {
        return "GlobalDataDictionary{" +
                "entries=" + entries +
                '}';
    }
}
