package com.jiva.cloud.juniatime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jiva.cloud.juniatime.repository.CheckRepository;
import com.jiva.cloud.juniatime.repository.TagRepository;
import com.jiva.cloud.juniatime.repository.availability.CheckAvailabilityDayRepository;
import com.jiva.cloud.juniatime.repository.availability.CheckAvailabilityHourRepository;
import com.jiva.cloud.juniatime.repository.availability.CheckAvailabilityMonthRepository;
import com.jiva.cloud.juniatime.repository.availability.CheckAvailabilityRepository;
import com.jiva.cloud.juniatime.repository.availability.CheckAvailabilityYearRepository;
import com.jiva.cloud.juniatime.repository.availability.TagAvailabilityDayRepository;
import com.jiva.cloud.juniatime.repository.availability.TagAvailabilityMonthRepository;
import com.jiva.cloud.juniatime.repository.availability.TagAvailabilityRepository;
import com.jiva.cloud.juniatime.repository.availability.TagAvailabilityYearRepository;
import com.jiva.cloud.juniatime.repository.availability.pingcount.PingCountCheckDayRepository;
import com.jiva.cloud.juniatime.repository.availability.pingcount.PingCountCheckHourRepository;
import com.jiva.cloud.juniatime.repository.availability.pingcount.PingCountCheckMonthRepository;
import com.jiva.cloud.juniatime.repository.availability.pingcount.PingCountCheckYearRepository;
import com.jiva.cloud.juniatime.repository.availability.pingcount.PingCountTagDayRepository;
import com.jiva.cloud.juniatime.repository.availability.pingcount.PingCountTagMonthRepository;
import com.jiva.cloud.juniatime.repository.availability.pingcount.PingCountTagYearRepository;

/**
 * @author highlander.dantas
 */
@Service
public abstract class TagRepositories {

    @Autowired
    protected TagRepository tagRepository;
    
    @Autowired
    protected CheckRepository checkRepository;
    
    @Autowired
    protected PingCountCheckHourRepository pingCountCheckHourRepository;
    
    @Autowired
    protected PingCountCheckDayRepository pingCountCheckDayRepository;
    
    @Autowired
    protected PingCountCheckMonthRepository pingCountCheckMonthRepository;
    
    @Autowired
    protected PingCountCheckYearRepository pingCountCheckYearRepository;
    
    @Autowired
    protected PingCountTagDayRepository pingCountTagDayRepository;
    
    @Autowired
    protected PingCountTagMonthRepository pingCountTagMonthRepository;
    
    @Autowired
    protected PingCountTagYearRepository pingCountTagYearRepository;

    @Autowired
    protected TagAvailabilityRepository tagAvailabilityRepository;

    @Autowired
    protected TagAvailabilityYearRepository tagAvailabilityYearRepository;

    @Autowired
    protected TagAvailabilityMonthRepository tagAvailabilityMonthRepository;
    
    @Autowired
    protected TagAvailabilityDayRepository tagAvailabilityDayRepository;
    
    @Autowired
    protected CheckAvailabilityRepository checkAvailabilityRepository; 
    
    @Autowired
    protected CheckAvailabilityYearRepository checkAvailabilityYearRepository;
    
    @Autowired
    protected CheckAvailabilityMonthRepository checkAvailabilityMonthRepository;
    
    @Autowired
    protected CheckAvailabilityDayRepository checkAvailabilityDayRepository;
    
    @Autowired
    protected CheckAvailabilityHourRepository checkAvailabilityHourRepository;

}