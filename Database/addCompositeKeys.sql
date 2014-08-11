--
-- Composite key for table `info`
--

ALTER TABLE info ADD UNIQUE INDEX(domain_id, header_field);

--
-- Composite key for table `port`
--

ALTER TABLE port ADD UNIQUE INDEX(domain_id, port_number);
