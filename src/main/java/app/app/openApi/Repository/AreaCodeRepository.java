package app.app.openApi.Repository;

import app.app.openApi.AreaCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaCodeRepository extends JpaRepository<AreaCode, Long> {
    //엔티티 클래스, pk의 데이터 타입
}