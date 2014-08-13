-- get a list of all the headers with their respective site
select domain_name, header_field, value from domain d LEFT JOIN info i on d.id=i.domain_id;

-- get the websites that have their server type listed
select domain_name, header_field, value from domain d LEFT JOIN info i on d.id=i.domain_id where header_field='server';

-- get the count of those sites
select count(*) from domain d LEFT JOIN info i on d.id=i.domain_id where header_field='server';

-- get a count of all total cwawlable sites
select count(*) from domain where crawl=1;

-- list them
select * from domain where crawl=1;

-- get a count of all total non-cwawlable sites
select count(*) from domain where crawl=0;

-- list them
select * from domain where crawl=0;

