import moment from 'moment';

export const getFormatedDate = (date: Date) => moment(date).format('DD.MM.YYYY, hh:mm');
