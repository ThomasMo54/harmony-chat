import { Platform } from 'react-native';

export const isMobile = Platform.OS === 'ios' || Platform.OS === 'android';
export const isDesktop = Platform.OS === 'windows' || Platform.OS === 'macos';