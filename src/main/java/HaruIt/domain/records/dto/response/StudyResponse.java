package HaruIt.domain.records.dto.response;

import HaruIt.domain.records.dto.request.StudyRequest;
import HaruIt.domain.records.entity.Study;
import java.time.LocalDate;

public record StudyResponse(
        Long recordId,
        LocalDate studyDate,
        String subject,
        int minutes
) {
    public static StudyResponse from(Study r) {
        return new StudyResponse(r.getId(), r.getStudyDate(), r.getSubject(), r.getMinutes());
    }
}
