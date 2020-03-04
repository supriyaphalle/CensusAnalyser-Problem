package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    List<IndiaCensusCSV> censusCSVList = null;
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder=CSVBuilderFactory.createCSVBuilder();
            List<IndiaCensusCSV> censusCSVList= csvBuilder.getCSVFilelist(reader,IndiaCensusCSV.class);
            return  censusCSVList.size();
            //  Iterator<IndiaCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            //return this.getCount(censusCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianStateCodeData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder=CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            return this.getCount(censusCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    private <E> int getCount(Iterator<E> iterator) {
        Iterable<E> csvIterable = () -> iterator;
        int namOfEntries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return namOfEntries;
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if(censusCSVList == null || censusCSVList.size()==0){
            throw new CensusAnalyserException("NO_CENSUS_DATA",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }

        Comparator<IndiaCensusCSV> comparing = Comparator.comparing(censusCSV -> censusCSV.state);
        this.sort( censusCSVList,comparing);
        String sortedStateCensus = new Gson().toJson(censusCSVList);
        return sortedStateCensus ;
    }

    private void sort(List<IndiaCensusCSV> censusCSVList,Comparator<IndiaCensusCSV> comparing) {
        for(int i=0;i<censusCSVList.size()-1;i++){
            for(int j=0;j<censusCSVList.size()-1;j++){
                IndiaCensusCSV census1= censusCSVList.get(j);
                IndiaCensusCSV census2= censusCSVList.get(j+1);
                if(comparing.compare(census1,census2)>0){
                    censusCSVList.set(j,census2);
                    censusCSVList.set(j+1,census1);
                }
            }
        }
    }


}
