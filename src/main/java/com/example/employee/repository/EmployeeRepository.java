package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //以下所有的*都代表变量

    //1.查询名字是*的第一个employee
    @Query(value = "select * from Employee where name=:name limit 1", nativeQuery = true)
    Employee findFirstEmployeeByName(@Param("name") String name);

    //2.找出Employee表中第一个姓名包含`*`字符并且薪资大于*的雇员个人信息
    @Query(value = "select * from Employee where name like %:str% and salary>:salary limit 1",
            nativeQuery = true)
    Employee findFirstEmployeeByNameAndSalary(@Param("str") String str, @Param("salary") int salary);

    //3.找出一个薪资最高且公司ID是*的雇员以及该雇员的姓名
    @Query(value = "select b.name from (select name,companyId,MAX(salary) from Employee group by companyId)b " +
            "where b.companyId=:companyId", nativeQuery = true)
    String findNameBySalaryAndCompanyId(@Param("companyId") int companyId);

    //4.实现对Employee的分页查询，每页两个数据
    @Query(value = "select e from Employee e", countQuery = "select count(e) from Employee e")
    Page<Employee> findALLEveryPageTwoRecords(Pageable pageable);

    //5.查找**的所在的公司的公司名称
    @Query(value = "select c.companyName from Company c,Employee e where e.companyId=c.id and e.name=:name")
    String findCompanyNameByName(@Param("name") String name);

    //6.将*的名字改成*,输出这次修改影响的行数
    @Modifying
    @Query(value = "update Employee set name =:newName where name =:originName",
            nativeQuery = true)
    int updateNameByName(@Param("originName") String originName, @Param("newName") String newName);

    //7.删除姓名是*的employee
    @Modifying
    @Query("delete from Employee where name=:name")
    void deleteByName(@Param("name") String name);
}
