import logging.handlers

import configs

logger = logging.getLogger('logger')
logger.setLevel(logging.INFO)

log_handler = logging.handlers.TimedRotatingFileHandler(configs.log_path + 'fc-ops.log', when='MIDNIGHT', interval=1,
                                                        backupCount=7)
log_handler.setFormatter(logging.Formatter("%(asctime)s - %(message)s"))
logger.addHandler(log_handler)
