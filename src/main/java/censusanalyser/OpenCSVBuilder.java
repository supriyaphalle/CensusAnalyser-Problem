package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCSVBuilder<E> implements ICSVBuilder {
    @Override
    public Iterator<E> getCSVFileIterator(Reader reader, Class csvclass) {
        return this.getCsvToBean(reader,csvclass).iterator();
    }

    @Override
    public List<E> getCSVFilelist(Reader reader, Class csvclass) {
        return this.getCsvToBean(reader,csvclass).parse();
    }

    private CsvToBean<E> getCsvToBean(Reader reader, Class csvclass) {
        try {
            CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(csvclass);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            return csvToBeanBuilder.build();
        } catch (IllegalStateException e) {
            throw new CSVBuilderException(e.getMessage(),
                    CSVBuilderException.ExceptionType.UNABLE_TO_PARSE);
        }
    }
}
